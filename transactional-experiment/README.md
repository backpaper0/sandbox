# Payara 4.1.1.154でDataSourceがJTAに参加してくれないっぽい件(ただしJPAの場合は参加してるっぽい)

Bytemanで色々見てる。

* JPAの場合はリクエスト中、最初の`Connection`取得時にJTAに参加してる感じがする

