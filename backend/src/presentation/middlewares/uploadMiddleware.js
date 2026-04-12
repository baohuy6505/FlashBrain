const multer = require('multer');
const { CloudinaryStorage } = require('multer-storage-cloudinary');
const cloudinary = require('cloudinary').v2;

// Cấu hình kho chứa trên Cloudinary
const storage = new CloudinaryStorage({
    cloudinary: cloudinary,
    params: {
        folder: 'FlashBrain_Avatars',
        allowed_formats: ['jpg', 'png', 'jpeg', 'webp'],
    }
});

const upload = multer({ storage: storage });

module.exports = upload;