import { useEffect, useState } from 'react';

export type Cursor = string | undefined;

export interface DataFetcher<T> {
    (cursor: Cursor): Promise<{
        data: Array<T>;
        nextCursor: Cursor;
    }>;
}

export function usePagenatedState<T>(dataFetcher: DataFetcher<T>): [Array<T> | undefined, () => Promise<void>] {
    const [data, setData] = useState<Array<T> | undefined>();
    const [cursor, setCursor] = useState<Cursor>();
    const fetchNext = async () => {
        const { data: addMe, nextCursor } = await dataFetcher(cursor);
        setData(data => [...(data || []), ...addMe]);
        setCursor(nextCursor);
    };
    useEffect(() => {
        fetchNext();
    }, []);
    return [data, fetchNext];
}
