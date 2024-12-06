const db = require('../config/database');

class UserModel {
    // 회원 생성
    async create(userData) {
        const query = `
            INSERT INTO Users (userId, name, salt, email, password, phone, status)
            VALUES (?, ?, ?, ?, ?, ?, 3)
        `;
        const [result] = await db.query(query, [
            userData.userId,
            userData.name,
            userData.salt,
            userData.email,
            userData.password,
            userData.phone
        ]);
        return result.insertId;
    }

    // ID로 회원 찾기
    async findById(userId) {
        const [rows] = await db.query('SELECT * FROM Users WHERE userId = ?', [userId]);
        return rows[0];
    }

    // 이메일로 회원 찾기
    async findByEmail(email) {
        const [rows] = await db.query('SELECT * FROM Users WHERE email = ?', [email]);
        return rows[0];
    }

    // 회원 정보 수정
    async updateUser(userId, userData) {
        const query = 'UPDATE Users SET name = ?, phone = ? WHERE userId = ?';
        const [result] = await db.query(query, [userData.name, userData.phone, userId]);
        return result.affectedRows > 0;
    }

    // 비밀번호 업데이트
    async updatePassword(userId, password, salt) {
        const query = 'UPDATE Users SET password = ?, salt = ? WHERE userId = ?';
        const [result] = await db.query(query, [password, salt, userId]);
        return result.affectedRows > 0;
    }

    // 로그인 시도 횟수 증가
    async incrementLoginTry(userId) {
        const query = 'UPDATE Users SET loginTry = loginTry + 1 WHERE userId = ?';
        await db.query(query, [userId]);
    }

    // 로그인 시도 횟수 초기화
    async resetLoginTry(userId) {
        const query = 'UPDATE Users SET loginTry = 0, lastLogin = CURRENT_TIMESTAMP WHERE userId = ?';
        await db.query(query, [userId]);
    }

    // 계정 상태 변경
    async updateStatus(userId, status) {
        const query = 'UPDATE Users SET status = ? WHERE userId = ?';
        const [result] = await db.query(query, [status, userId]);
        return result.affectedRows > 0;
    }
}

module.exports = new UserModel();