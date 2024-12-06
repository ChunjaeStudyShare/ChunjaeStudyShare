const db = require('../config/database');

/* 
table : Member

column :
userId; // 아이디
name; // 이름
salt; // 솔트
email; // 이메일
password; // 비밀번호
phone; // 휴대폰 번호
status; // 0: 활동 중, 1: 휴면, 2: 탈퇴(강퇴), 3: 미인증
lastLogin; // 마지막 로그인 시간
loginTry; // 로그인 시도 횟수 최대 5회까지 가능
*/
class UserModel {
    async findById(userId) {
        const [rows] = await db.query(
            'SELECT * FROM Member WHERE userId = ?',
            [userId]
        );
        return rows[0];
    }

    async findByEmail(email) {
        const [rows] = await db.query(
            'SELECT * FROM Member WHERE email = ?',
            [email]
        );
        return rows[0];
    }

    async updateLastLogin(userId) {
        await db.query(
            'UPDATE Member SET lastLogin = NOW() WHERE userId = ?',
            [userId]
        );
    }

    async updateStatus(userId, status) {
        await db.query(
            'UPDATE Member SET status = ? WHERE userId = ?',
            [status, userId]
        );
    }

    async updateLoginTry(userId) {
        await db.query(
            'UPDATE Member SET loginTry = loginTry + 1 WHERE userId = ?',
            [userId]
        );
    }

    async resetLoginTry(userId) {
        await db.query(
            'UPDATE Member SET loginTry = 0 WHERE userId = ?',
            [userId]
        );
    }

    async insertUser(user) {
        await db.query('INSERT INTO Member (userId, name, salt, email, password, phone, status, lastLogin, loginTry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)',
            [user.userId, user.name, user.salt, user.email, user.password, user.phone, 3, user.lastLogin, user.loginTry]
        );
    }
    
    async updatePassword(userId, password, salt) {
        await db.query('UPDATE Member SET password = ?, salt = ? WHERE userId = ?', [password, salt, userId]);
    }

}

module.exports = new UserModel();