# VagrantとAnsible

## 起動

```console
vagrant up
```

## ssh

```console
vagrant ssh
```

## 構成変更

例えば`playbook.yml`にGitのインストールを加えた時。

```diff
diff --git a/hello-vagrant/playbook.yml b/hello-vagrant/playbook.yml
index c834caa..bc276f7 100644
--- a/hello-vagrant/playbook.yml
+++ b/hello-vagrant/playbook.yml
@@ -4,3 +4,6 @@
   - name: install sl
     apt:
       name: sl
+  - name: install git
+    apt:
+      name: git
```

```console
vagrant provision
```

> `playbook.yml`にGitのインストールを加えた時

と表現したけれど、サーバー構成にGitが加わった時、と捉えた方が構成管理的には良さそう感。

