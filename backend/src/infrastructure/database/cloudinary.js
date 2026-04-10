const cloudinary = require('cloudinary').v2;
const connnectCloudinary = async () => {

try {
        cloudinary.config({
        cloud_name: process.env.CLOUDINARY_CLOUD_NAME,
        api_key: process.env.CLOUDINARY_API_KEY,
        api_secret: process.env.CLOUDINARY_API_SECRET,
        });

        const result = await cloudinary.api.ping();
        console.log(`Cloudinary Connected: ${result.status}`);

    } catch (error) {
        console.error('Failed to connect to Cloudinary:', error.message);
    }

};



module.exports = connnectCloudinary;