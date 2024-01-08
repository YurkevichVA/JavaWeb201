document.addEventListener('DOMContentLoaded', function() {

    const authLogInButton = document.getElementById('auth-login-btn');
    if( authLogInButton ) {
        authLogInButton.addEventListener("click", authLogInButtonClick);
    } else {
        console.error("auth-login-btn not found");
    }

    const addCoinsButton = document.getElementById('add-coins-btn');
    if(addCoinsButton) {
        addCoinsButton.addEventListener('click', addCoinsButtonClick);
    } else {
        console.error('add-coins-btn not found');
    }

    const logoutButton = document.getElementById('logout-btn');
    if(logoutButton) {
        logoutButton.addEventListener('click', logoutButtonClick);
    } else {
        console.error('logout-btn not found');
    }

    const buyCharacterButtons = document.querySelectorAll('.buy-character-btn');

    buyCharacterButtons.forEach(button => {
        button.addEventListener('click', buyCharacterButtonClick);
    });

});

function authLogInButtonClick() {
    [authLoginInput, authPasswordInput, authMessage] = getAuthElements();

    if (authLoginInput.value.length === 0 || authPasswordInput.value.length === 0) {
        authMessage.innerText = "Логін та пароль не можуть бути порожніми";
        authPasswordInput.value = '';
        return;
    }

    const appContext = getAppContext();

    fetch (`${appContext}/auth?login=${authLoginInput.value}&password=${authPasswordInput.value}`, { method: 'GET' })
        .then( r => {
            if ( r.status !== 200 ) {
                authMessage.innerText = "Автентифікацію відхилено";
            } else {
                r.text().then(base64encodedText => {
                    const token = JSON.parse(atob(base64encodedText));
                    if(typeof token.jti === 'undefined') {
                        authMessage.innerText = "Помилка одержання токену";
                        return;
                    }
                    window.localStorage.setItem('token', base64encodedText);
                    window.location = `${appContext}/profile`;;
            });
        }
    });
}

function getAuthElements() {

    const authLoginInput = document.getElementById("auth-login-input");
    if(!authLoginInput) {
        throw '#auth-login-input not found';
    }

    const authPasswordInput = document.getElementById("auth-password-input");
    if(!authPasswordInput) {
        throw '#auth-password-input not found';
    }

    const authMessage = document.getElementById("auth-message");
    if(!authMessage) {
        throw '#auth-message not found';
    }

    return [authLoginInput, authPasswordInput, authMessage];
}

function getAppContext() {
    return '/' + window.location.pathname.split('/')[1];
}

function addCoinsButtonClick() {
    const appContext = getAppContext(); // Assuming you have a function to get the app context

    fetch(`${appContext}/profile`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ action: 'addCoins' }), // You can adjust the payload as needed
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response; // If your servlet returns JSON, otherwise adjust accordingly
        })
        .then(data => {
            // Handle the response data
            console.log('Success:', data);
            window.location.reload();
        })
        .catch(error => {
            // Handle errors
            console.error('Error:', error);
        });
}

function buyCharacterButtonClick(event) {
    const appContext = getAppContext(); // Assuming you have a function to get the app context

    const characterId = event.currentTarget.getAttribute('data-character-id');
    const characterCost = event.currentTarget.getAttribute('data-character-cost');

    fetch(`${appContext}/shop`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ characterId, characterCost }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response; // If your servlet returns JSON, otherwise adjust accordingly
        })
        .then(data => {
            // Handle the response data
            console.log('Success:', data);
            window.location.reload(); // You might want to update the UI in a more specific way
        })
        .catch(error => {
            // Handle errors
            console.error('Error:', error);
        });
}

function logoutButtonClick() {
    document.cookie = 'authToken=;';
    window.location.reload();
}