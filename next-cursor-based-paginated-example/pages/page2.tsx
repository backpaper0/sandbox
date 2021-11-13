import { getPosts, Posts } from 'lib/posts';
import Head from 'next/head';
import useSWRInfinite from 'swr/infinite';

function getKey(pageIndex: number, previousPageData: Posts | null) {
    return ["/api/posts", previousPageData && previousPageData.nextCursor];
}

async function fetcher(url: string, cursor: string): Promise<Posts> {
    return await getPosts(cursor);
}

export default function Page2() {
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
                <title>SWR version</title>
            </Head>

            <main>
                {posts.map(post => <p key={post}>{post}</p>)}
                <button onClick={readMore}>Read more</button>
            </main>
        </div>
    )
}
