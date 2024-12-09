const router = require('express').Router();
const db = require('../config/database');

// 단일 레코드 조회 테스트
router.get('/db/single/:id', async (req, res) => {
    const result = await db.query('SELECT * FROM Member WHERE userId = ?', [req.params.id]);
    res.json(result[0]);
});

// 여러 레코드 조회 테스트
router.get('/db/multiple', async (req, res) => {
    const result = await db.query('SELECT * FROM Member LIMIT 100');
    res.json(result[0]);
});

// JOIN 쿼리 테스트
router.get('/db/join/:id', async (req, res) => {
    const result = await db.query(`
        SELECT m.*, e.code 
        FROM Member m 
        LEFT JOIN EmailCode e ON m.userId = e.userId 
        WHERE m.userId = ?
    `, [req.params.id]);
    res.json(result[0]);
});

// INSERT 테스트
router.post('/db/insert', async (req, res) => {
    const start = process.hrtime();
    const result = await db.query(
        'INSERT INTO TestTable (field1, field2) VALUES (?, ?)',
        [req.body.field1, req.body.field2]
    );
    const diff = process.hrtime(start);
    const time = diff[0] * 1e3 + diff[1] * 1e-6;
    res.json({ time, result: result[0] });
});

module.exports = router; 