export async function getPosts(cursor: string | null = null) {
    if (cursor === null || cursor === "foo") {
        return { "posts": ["post1", "post2", "post3"], "nextCursor": "bar" }
    } else if (cursor === "bar") {
        return { "posts": ["post4", "post5", "post6"], "nextCursor": "baz" }
    } else if (cursor === "baz") {
        return { "posts": ["post7"], "nextCursor": "qux" }
    }
    return { "posts": [], "nextCursor": "qux" }
}
