const jwt = require('jsonwebtoken');

module.exports = {
    generateToken: (user) => jwt.sign({
        userId: user.id,
        email: user.email,
        name: user.name,
    }, process.env.JWT_SECRET, { expiresIn: '1h' })
};
