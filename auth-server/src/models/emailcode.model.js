const db = require('../config/database');

class EmailCodeModel {
    // 인증 코드 저장
    async create(userId, code) {
        const query = `
            INSERT INTO EmailCode (userId, code)
            VALUES (?, ?)
        `;
        const [result] = await db.query(query, [userId, code]);
        return result.insertId;
    }

    // 최근 인증 코드 조회
    async getLatestCode(userId) {
        const query = `
            SELECT * FROM EmailCode 
            WHERE userId = ? 
            ORDER BY createdAt DESC 
            LIMIT 1
        `;
        const [rows] = await db.query(query, [userId]);
        return rows[0];
    }

    // 인증 코드 검증
    async verifyCode(userId, code) {
        const query = `
            SELECT * FROM EmailCode 
            WHERE userId = ? AND code = ? 
            AND createdAt > DATE_SUB(NOW(), INTERVAL 24 HOUR)
            ORDER BY createdAt DESC 
            LIMIT 1
        `;
        const [rows] = await db.query(query, [userId, code]);
        return rows[0];
    }

    // 사용된 인증 코드 삭제
    async deleteCode(userId) {
        const query = 'DELETE FROM EmailCode WHERE userId = ?';
        await db.query(query, [userId]);
    }

    // 비밀번호 재설정용 토큰 검증 (1시간)
    async verifyResetToken(userId, token) {
        const query = `
            SELECT * FROM EmailCode 
            WHERE userId = ? AND code = ? 
            AND createdAt > DATE_SUB(NOW(), INTERVAL 1 HOUR)
            ORDER BY createdAt DESC 
            LIMIT 1
        `;
        const [rows] = await db.query(query, [userId, token]);
        return rows[0];
    }
}

module.exports = new EmailCodeModel();