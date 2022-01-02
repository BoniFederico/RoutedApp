module.exports = {
  publicPath: "/routedapp/",
  devServer: {
    proxy: {
      "^/routed/api/*": {
        target: "http://localhost:8080",
        secure: false,
      },
    },
  },
};
