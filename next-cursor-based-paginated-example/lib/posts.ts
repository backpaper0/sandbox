
export type Post = string;

export interface Posts {
    posts: Array<Post>;
    nextCursor: string;
}

export function getPosts(cursor: string | null = null): Promise<Posts> {
    return new Promise(resolve => {
        window.setTimeout(() => {
            resolve(getPostsInternal(cursor));
        }, 200);
    });
}

function getPostsInternal(cursor: string | null = null): Posts {
    if (cursor === null || cursor === "foo") {
        return { posts: ["post1", "post2", "post3"], nextCursor: "bar" }
    } else if (cursor === "bar") {
        return { posts: ["post4", "post5", "post6"], nextCursor: "baz" }
    } else if (cursor === "baz") {
        return { posts: ["post7"], nextCursor: "qux" }
    }
    return { posts: [], nextCursor: "qux" }
}
