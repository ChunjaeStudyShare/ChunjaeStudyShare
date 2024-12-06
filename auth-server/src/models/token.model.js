const db = require('../config/database');

class TokenModel {
    // 화이트리스트에 토큰 추가
    async addToWhitelist(userId, jti, expiresAt) {
        const [result] = await db.query(
            'INSERT INTO ActiveTokens (userId, jti, expiresAt, createdAt) VALUES (?, ?, ?, NOW())',
            [userId, jti, expiresAt]
        );
        return result.insertId;
    }

    // 토큰 유효성 검증
    async isValidToken(userId, jti) {
        const [tokens] = await db.query(
            'SELECT * FROM ActiveTokens WHERE userId = ? AND jti = ? AND expiresAt > NOW()',
            [userId, jti]
        );
        return tokens.length > 0;
    }

    //만료된 토큰 삭제 (정리용)
    async removeExpiredTokens() {
        await db.query('DELETE FROM ActiveTokens WHERE expiresAt <= NOW()');
    }

}

module.exports = new TokenModel();