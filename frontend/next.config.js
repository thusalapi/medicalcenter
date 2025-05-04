/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: true,
    output: 'export', // Enable static exports
    images: {
        unoptimized: true, // Required for static export
    },
    trailingSlash: true, // Better for static hosting
    // Configure API proxy for development
    async rewrites() {
        return [
            {
                source: '/api/:path*',
                destination: 'http://localhost:8080/api/:path*',
            },
        ]
    },
}

module.exports = nextConfig