// Allow fallback to root index.html file
if (config.devServer) {
    config.output.publicPath = "/";
    config.devServer.publicPath = "/";
    config.devServer.historyApiFallback = true;
}
