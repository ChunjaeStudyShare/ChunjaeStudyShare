const db = require('../config/database');

class UserModel {
    async findById(userId) {
        const [rows] = await db.query(
            'SELECT * FROM Member WHERE id = ?',
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
            'UPDATE Member SET lastLogin = NOW() WHERE id = ?',
            [userId]
        );
    }

    async updateStatus(userId, status) {
        await db.query(
            'UPDATE Member SET status = ? WHERE id = ?',
            [status, userId]
        );
    }

    async updateLoginTry(userId) {
        await db.query(
            'UPDATE Member SET loginTry = loginTry + 1 WHERE id = ?',
            [userId]
        );
    }

    async resetLoginTry(userId) {
        await db.query(
            'UPDATE Member SET loginTry = 0 WHERE id = ?',
            [userId]
        );
    }
}

module.exports = new UserModel();