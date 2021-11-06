import { getPosts } from 'lib/posts';
import type { NextPage } from 'next';
import Head from 'next/head';
import useSWRInfinite from 'swr/infinite';

function getKey(pageIndex: number, previousPageData: { nextCursor: string } | null) {
    return ["/api/posts", previousPageData && previousPageData.nextCursor];
}

async function fetcher(url: string, cursor: string) {
    return await getPosts(cursor);
}

const Page1: NextPage = () => {
    const { data, error, isValidating, mutate, size, setSize } = useSWRInfinite(getKey, fetcher);
    const readMore = () => {
        setSize(size + 1);
    };
    if (!data) {
        return null;
    }
    const posts = data.flatMap(({ posts }) => posts);
    return (
        <div>
            <Head>
                <title>useEffect version</title>
            </Head>

            <main>
                {posts.map(post => <p key={post}>{post}</p>)}
                <button onClick={readMore}>Read more</button>
            </main>
        </div>
    )
}

export default Page1
