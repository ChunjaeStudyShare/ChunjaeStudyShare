const UserModel = require('../models/user.model');

class UserService {
    async register(user) {
        console.log(user);
        return await UserModel.create(user);
    }
    async 
}

module.exports = new UserService();