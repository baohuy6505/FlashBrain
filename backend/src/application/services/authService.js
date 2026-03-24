const IAuthService = require('../interfaces/IAuthService');
const bcrypt = require('bcryptjs');
const { v4: uuidv4 } = require('uuid');
const userRepository = require('../../infrastructure/repositories/userRepository');
const UserResponseDto = require('../dtos/authDto/userResponseDto');
const { generateToken } = require('../utils/jwt');

class AuthService extends IAuthService {
    async register(registerDto) {
        
        // 1. Dữ liệu lúc này đã đi qua RegisterRequestDto nên chắc chắn an toàn và sạch sẽ
        const { email, password, name } = registerDto;


        // 2. Kiểm tra email tồn tại chưa
        const existingUser = await userRepository.findByEmail(email);
        if (existingUser) {
            throw new Error('Email đã được sử dụng');
        }

        // 3. Băm mật khẩu
        const passwordHash = await bcrypt.hash(password, 10);

        // 4. Lưu vào DB (Lưu ý field password_hash khớp DB Document)
        const newUser = await userRepository.create({
            _id: uuidv4(),
            name,   
            email,
            passwordHash: passwordHash
        });

        // 5. Trả về token và thông tin user đã qua DTO
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
        const user = await userRepository.findByEmail(email);
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