const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
    app.use(
        '/dragon',
        createProxyMiddleware({
            target: 'http://192.168.86.7:8080',
            changeOrigin: true,
        })
    );
};