const db = require('../config/database');

class TokenModel {
    // 토큰 저장
    async saveToken(userId, jti, expiresAt) {
        const query = `
            INSERT INTO ActiveTokens (userId, jti, expiresAt)
            VALUES (?, ?, ?)
        `;
        const [result] = await db.query(query, [userId, jti, expiresAt]);
        return result.insertId;
    }

    // 토큰 검증
    async verifyToken(userId, jti) {
        const query = `
            SELECT * FROM ActiveTokens 
            WHERE userId = ? AND jti = ? AND expiresAt > CURRENT_TIMESTAMP
        `;
        const [rows] = await db.query(query, [userId, jti]);
        return rows[0];
    }

    // 만료된 토큰 삭제
    async removeExpiredTokens() {
        const query = 'DELETE FROM ActiveTokens WHERE expiresAt <= CURRENT_TIMESTAMP';
        await db.query(query);
    }

    // 특정 사용자의 모든 토큰 삭제 (로그아웃)
    async removeUserTokens(userId) {
        const query = 'DELETE FROM ActiveTokens WHERE userId = ?';
        await db.query(query, [userId]);
    }
}

module.exports = new TokenModel();