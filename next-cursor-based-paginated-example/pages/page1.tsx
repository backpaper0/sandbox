import { getPosts } from 'lib/posts';
import type { NextPage } from 'next';
import Head from 'next/head';
import { useEffect, useReducer, useState } from 'react';

interface InitialAction {
    kind: "initial";
    posts: Array<string>;
}

interface AdditionalAction {
    kind: "additional";
    posts: Array<string>;
}

type Action = InitialAction | AdditionalAction;

function reducer(state: Array<string>, action: Action): Array<string> {
    switch (action.kind) {
        case "initial":
            return action.posts;
        case "additional":
            return [...state, ...action.posts];
        default:
            return state;
    }
}

const initialState: Array<string> = [];

const Page1: NextPage = () => {
    const [posts, dispatch] = useReducer(reducer, initialState);
    const [cursor, setCuror] = useState<string | undefined>();
    useEffect(() => {
        const load = async () => {
            const { posts, nextCursor } = await getPosts();
            dispatch({ kind: "initial", posts, });
            setCuror(nextCursor);
        };
        load();
    }, []);
    const readMore = () => {
        const load = async () => {
            const { posts, nextCursor } = await getPosts(cursor);
            dispatch({ kind: "additional", posts, });
            setCuror(nextCursor);
        };
        load();
    };
    if (!posts.length) {
        return null;
    }
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
