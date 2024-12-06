const Joi = require('joi');

module.exports = {
    register: Joi.object({
        id: Joi.string()
            .pattern(new RegExp('^[a-z0-9]+$'))
            .required()
            .messages({
                'string.pattern.base': '아이디는 영어 소문자와 숫자만 사용할 수 있습니다.'
            }),
        email: Joi.string().email().required(),
        password: Joi.string()
            .min(8)
            .pattern(new RegExp('^[A-Za-z0-9!@#$%^&*(),.?":{}|<>]+$'))
            .required()
            .messages({
                'string.pattern.base': '비밀번호는 최소 8자 이상, 대소문자, 숫자, 특수문자만 사용할 수 있습니다.'
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
                'string.pattern.base': '비밀번호는 최소 8자 이상, 대소문자, 숫자, 특수문자만 사용할 수 있습니다.'
            })
    })
}