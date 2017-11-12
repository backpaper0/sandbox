# Hello, yarn!

準備

```
mkdir hello-yarn
cd hello-yarn
yarn init --yes
yarn add ava --dev
```

`package.json`を次のように編集する。

```diff
diff --git a/hello-yarn/package.json b/hello-yarn/package.json
index 8a95a14..c57e0af 100644
--- a/hello-yarn/package.json
+++ b/hello-yarn/package.json
@@ -3,6 +3,9 @@
   "version": "1.0.0",
   "main": "index.js",
   "license": "MIT",
+  "scripts": {
+    "test": "ava"
+  },
   "devDependencies": {
     "ava": "^0.23.0"
   }
```

例として`test.js`を作る。

```
cat <<EOS>test.js
import test from 'ava'

test('hello', t => {
    t.pass()
})
EOS
```

実行してみる。

```
yarn run test
```

