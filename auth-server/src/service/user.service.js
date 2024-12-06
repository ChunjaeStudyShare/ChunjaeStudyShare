const crypto = require('crypto');
const UserModel = require('../models/user.model');

class UserService {
    async register(user) {
        //salt 생성 crypto 모듈 사용(16바이트 랜덤 문자열 생성)
        const salt = crypto.randomBytes(16).toString('hex');
        user.salt = salt;
        return await UserModel.create(user);
    }
}

module.exports = new UserService();