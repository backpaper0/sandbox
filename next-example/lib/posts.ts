export interface Post {
    id: string;
    text: string;
}

const posts = ['foo', 'bar', 'baz', 'qux'].map((text, index) => ({ id: index.toString(), text }));

export async function findPost(id: string) {
    return posts.find(p => p.id === id);
}

export async function getAllPostsIds() {
    return posts.map(p => ({
        params: { id: p.id }
    }));
}
