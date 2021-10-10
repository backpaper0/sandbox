# next-isr-demo

Next.jsの[Incremental Static Regeneration](https://vercel.com/docs/concepts/next.js/incremental-static-regeneration)を試す。

`getStaticPaths`は`fallback: 'blocking'`を設定する。
そうすることでフォールバック時にレンダリングを待ってくれる。

フォールバック時に`getStaticProps`が呼び出される。
該当するデータがなかった場合は`{ notFound: true }`を返すようにしておく。

静的ファイルをCloudFrontにデプロイする場合、バックエンドはどうしておくのが良いだろう？
というか全体的にどういう感じでデプロイすりゃ良いんだろう？
