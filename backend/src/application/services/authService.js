const IAuthService = require('../interfaces/IAuthService');
const bcrypt = require('bcryptjs');
const { v4: uuidv4 } = require('uuid');
const userRepository = require('../../infrastructure/repositories/userRepository');
const UserResponseDto = require('../dtos/authDto');
const { generateToken } = require('../utils/jwt');

class AuthService extends IAuthService {
    async register(body) {
        const email = body.email?.trim().toLowerCase();
        const password = body.password;
        const name = body.name?.trim() || '';

        // 1. Kiểm tra email tồn tại chưa
        const existingUser = await userRepository.getUserByEmail(email);
        if (existingUser) {
            throw new Error('Email đã được sử dụng');
        }

        // 2. Băm mật khẩu
        const passwordHash = await bcrypt.hash(password, 10);

        // 3. Lưu vào DB (Lưu ý field password_hash khớp DB Document)
        const newUser = await userRepository.createUser({
            _id: uuidv4(),
            name,   
            email,
            passwordHash: passwordHash
        });

        // 4. Trả về token và thông tin user đã qua DTO
        // Bọc data vào trong một Object {} trước khi truyền vào hàm
        const token = generateToken({ 
            userId: newUser._id, 
            role: newUser.role 
        });

        return {
            token,
            user: new UserResponseDto(newUser)
        };
    }

    // --- CHỨC NĂNG ĐĂNG NHẬP ---
    async login(body) {
        const email = body.email?.trim().toLowerCase();
        const password = body.password;

        // 1. Tìm user
        const user = await userRepository.getUserByEmail(email);
        if (!user) {
            throw new Error('Email hoặc mật khẩu không chính xác');
        }

        // 2. Kiểm tra mật khẩu
        const isMatch = await bcrypt.compare(password, user.passwordHash);
        if (!isMatch) {
            throw new Error('Email hoặc mật khẩu không chính xác');
        }

        // 3. Trả về token và thông tin user đã qua DTO
        const token = generateToken({ 
            userId: user._id, 
            role: user.role 
        });

        return {
            token,
            user: new UserResponseDto(user)
        };
    }
}
module.exports = new AuthService();