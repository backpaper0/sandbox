# next-static-html-export-demo

## パスパラメーターは使えなさそう

~~`next export`では事前解決できない動的ページは作れなさそうなのでパスパラメーターではなくクエリーで受け取って`getInitialProps`することで対応する。~~

ルーターからクエリパラメーターを取得できた。

```typescript
import { useRouter } from 'next/router'
```

```typescript
const router = useRouter();
const id = router.query.id as string;
```
