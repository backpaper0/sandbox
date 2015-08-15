function encodeBase64(src) {
    var cs = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".split("");
    var out = "";
    var read;
    var index = 0;
    var written = 0;
    var i = 0;
    while (true) {
        read = src.charCodeAt(i++);
        if (isNaN(read)) {
            break;
        }
        index = read >> 2;
        out += cs[index & 0x3f];
        written++;

        index = read << 4;
        read = src.charCodeAt(i++);
        if (isNaN(read)) {
            out += cs[index & 0x3f];
            written++;
            break;
        }
        index |= read >> 4;
        out += cs[index & 0x3f];
        written++;

        index = read << 2;
        read = src.charCodeAt(i++);
        if (isNaN(read)) {
            out += cs[index & 0x3f];
            written++;
            break;
        }
        index |= read >> 6;
        out += cs[index & 0x3f];
        written++;

        index = read;
        out += cs[index & 0x3f];
        written++;
    }
    while (written % 4 != 0) {
        out += '=';
        written++;

    }
    return out;
}
