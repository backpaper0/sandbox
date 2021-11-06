import { useEffect, useState } from 'react';

export type Cursor = string | undefined;

export interface DataFetcher<T> {
    (cursor: Cursor): Promise<{
        data: Array<T>;
        nextCursor: Cursor;
    }>;
}

export function usePagenatedState<T>(dataFetcher: DataFetcher<T>): [Array<T> | undefined, () => Promise<void>, boolean] {
    const [data, setData] = useState<Array<T> | undefined>();
    const [cursor, setCursor] = useState<Cursor>();
    const [fetching, setFetching] = useState(false);
    const fetchNext = async () => {
        setFetching(true);
        try {
            const { data: addMe, nextCursor } = await dataFetcher(cursor);
            setData(data => [...(data || []), ...addMe]);
            setCursor(nextCursor);
        } finally {
            setFetching(false);
        }
    };
    useEffect(() => {
        fetchNext();
    }, []);
    return [data, fetchNext, fetching];
}
