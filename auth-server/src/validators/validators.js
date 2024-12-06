const Joi = require('joi');
const errorMessage = require('../errormessage/error.message');
module.exports = {
    register: Joi.object({
        id: Joi.string()
            .pattern(new RegExp('^[a-z0-9]{4,12}$'))
            .required()
            .messages({
                'string.pattern.base': errorMessage.INVALID_ID_FORMAT
            }),
        email: Joi.string().email().required(),
        password: Joi.string()
            .min(8)
            .pattern(new RegExp('^[A-Za-z0-9!@#$%^&*(),.?":{}|<>]+$'))
            .required()
            .messages({
                'string.pattern.base': errorMessage.INVALID_PASSWORD_FORMAT
            }),
        name: Joi.string().min(2).max(30).required(),
        phone: Joi.string().pattern(new RegExp('^[0-9]{10,11}$')).optional(),
    }),

    login: Joi.object({
        id: Joi.string().required(),
        password: Joi.string().required()
    }),

    updatePassword: Joi.object({
        currentPassword: Joi.string().required(),
        newPassword: Joi.string()
            .min(8)
            .pattern(new RegExp('^[A-Za-z0-9!@#$%^&*(),.?":{}|<>]+$'))
            .required()
            .messages({
                'string.pattern.base': errorMessage.INVALID_PASSWORD_FORMAT
            })
    })
}