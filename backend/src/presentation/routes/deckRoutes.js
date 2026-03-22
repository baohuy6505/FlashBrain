const express = require('express');
const router = express.Router();

router.post('/', (req, res) => res.json({ message: "TODO: API Tạo bộ thẻ mới" }));
router.get('/', (req, res) => res.json({ message: "TODO: API Lấy danh sách bộ thẻ của User" }));
router.put('/:id', (req, res) => res.json({ message: "TODO: API Sửa tên bộ thẻ" }));
router.delete('/:id', (req, res) => res.json({ message: "TODO: API Xóa mềm (is_deleted) bộ thẻ" }));

module.exports = router;