```bash
npm i -D typescript @types/node
npm i -D eslint @typescript-eslint/parser @typescript-eslint/eslint-plugin
npx tsc --init
npx eslint --init
```

```diff
diff --git a/typescript-example/tsconfig.json b/typescript-example/tsconfig.json
index cec4a3a4..bf1c1b7a 100644
--- a/typescript-example/tsconfig.json
+++ b/typescript-example/tsconfig.json
@@ -2,8 +2,8 @@
   // Visit https://aka.ms/tsconfig to read more about this file
   "compilerOptions": {
     // File Layout
-    // "rootDir": "./src",
-    // "outDir": "./dist",
+    "rootDir": "./src",
+    "outDir": "./build",
 
     // Environment Settings
     // See also https://aka.ms/tsconfig/module
```