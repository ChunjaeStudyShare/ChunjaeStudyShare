const db = require('../config/database');

class UserModel {
    // 회원 생성
    async create(userData) {
        const query = `
            INSERT INTO Member (userId, name, salt, email, password, phone, status)
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
        const [rows] = await db.query('SELECT * FROM Member WHERE userId = ?', [userId]);
        return rows[0];
    }

    // 이메일로 회원 찾기
    async findByEmail(email) {
        const [rows] = await db.query('SELECT * FROM Member WHERE email = ?', [email]);
        return rows[0];
    }

    // 전화번호로 회원 찾기
    async findByPhone(phone) {
        const [rows] = await db.query('SELECT * FROM Member WHERE phone = ?', [phone]);
        return rows[0];
    }

    // 회원 정보 수정
    async updateUser(userId, userData) {
        const query = 'UPDATE Member SET name = ?, phone = ? WHERE userId = ?';
        const [result] = await db.query(query, [userData.name, userData.phone, userId]);
        return result.affectedRows > 0;
    }

    // 비밀번호 업데이트
    async updatePassword(userId, password, salt) {
        const query = 'UPDATE Member SET password = ?, salt = ? WHERE userId = ?';
        const [result] = await db.query(query, [password, salt, userId]);
        return result.affectedRows > 0;
    }

    // 로그인 시도 횟수 증가
    async incrementLoginTry(userId) {
        const query = 'UPDATE Member SET loginTry = loginTry + 1 WHERE userId = ?';
        await db.query(query, [userId]);
    }

    // 로그인 시도 횟수 초기화
    async resetLoginTry(userId) {
        const query = 'UPDATE Member SET loginTry = 0, lastLogin = CURRENT_TIMESTAMP WHERE userId = ?';
        await db.query(query, [userId]);
    }

    // 계정 상태 변경
    async updateStatus(userId, status) {
        const query = 'UPDATE Member SET status = ? WHERE userId = ?';
        const [result] = await db.query(query, [status, userId]);
        return result.affectedRows > 0;
    }

    // 아이디 중복 체크
    async checkId(userId) {
        //Exist 를 이용하여 쿼리 최적화
        const query = 'SELECT EXISTS(SELECT 1 FROM Member WHERE userId = ?) AS is_duplicate';
        const [rows] = await db.query(query, [userId]);
        return rows[0].is_duplicate;
    }

    // 이메일 중복 체크
    async checkEmail(email) {
        const query = 'SELECT EXISTS(SELECT 1 FROM Member WHERE email = ?) AS is_duplicate';
        const [rows] = await db.query(query, [email]);
        return rows[0].is_duplicate;
    }
}

module.exports = new UserModel();