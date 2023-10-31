package step.learning.dto.entities;

import java.util.Date;

public class AuthToken {
    private String jti  ;   // id
    private String sub  ;   // user-id
    private Date exp    ;   // expires
    private Date iat    ;   // issued at

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public Date getIat() {
        return iat;
    }

    public void setIat(Date iat) {
        this.iat = iat;
    }
}
/*
Ідея - назвати поля сутності як у стандарті JWT.
 */
