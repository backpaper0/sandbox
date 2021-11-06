import type { NextPage } from 'next'
import Head from 'next/head'
import Link from 'next/link';

const Home: NextPage = () => {
  return (
    <div>
      <Head>
        <title>Next cursor based paginated example</title>
      </Head>

      <main>
        <h1>
          Welcome to <a href="https://nextjs.org">Next.js!</a>
        </h1>

        <p>
          <Link href="/page1">useEffect version</Link>
        </p>
        <p>
          <Link href="/page2">SWR version</Link>
        </p>
      </main>
    </div>
  )
}

export default Home
