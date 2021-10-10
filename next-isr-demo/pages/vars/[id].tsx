import type { GetStaticPaths, GetStaticProps, InferGetStaticPropsType } from 'next'
import Head from 'next/head'
import styles from '../../styles/Home.module.css'
import Link from 'next/link'

const Var = ({ content }: InferGetStaticPropsType<typeof getStaticProps>) => {
    return (
        <div className={styles.container}>
            <Head>
                <title>{content.id} {content.name}</title>
            </Head>
            <main className={styles.main}>
                <h1 className={styles.title}>{content.name}</h1>
                <p><Link href={`/vars/${content.next}`}><a>Next var {content.next}</a></Link></p>
            </main>
        </div>
    )
}

export const getStaticPaths: GetStaticPaths = async () => {
    return ({
        paths: [
            { params: { id: '1' } },
            { params: { id: '2' } },
            { params: { id: '3' } },
        ],
        fallback: 'blocking'
    });
};

export const getStaticProps: GetStaticProps = async ({ params }) => {
    const id = params!.id as string;
    const v = vars[id];
    if (!v){
        return ({
            notFound: true
        });
    }
    const content = { id, ...v };
    return {
        props: { content },
//        revalidate: 60,
    }
}

const vars: { [key: string]: { name: string; next: string; } } = {
    '1': { name: 'foo', next: '2' },
    '2': { name: 'bar', next: '3' },
    '3': { name: 'baz', next: '4' },
    '4': { name: 'qux', next: '5' },
};

export default Var;