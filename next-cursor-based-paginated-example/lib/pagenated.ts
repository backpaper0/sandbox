import { useState, useEffect } from 'react';

export type Cursor = string | undefined;

export interface FetchResult<T> {
    data: Array<T>;
    nextCursor: Cursor;
}

export interface DataFetcher<T> {
    (cursor: Cursor): Promise<FetchResult<T>>;
}

export interface FetchNext {
    (): Promise<void>;
}

export function usePagenatedState<T>(dataFetcher: DataFetcher<T>): [Array<T> | undefined, FetchNext] {
    const [data, setData] = useState<Array<T> | undefined>();
    const [cursor, setCursor] = useState<Cursor>();
    const fetchNext = async () => {
        const fetchResult = await dataFetcher(cursor);
        setData(data => [...(data || []), ...fetchResult.data]);
        setCursor(fetchResult.nextCursor);
    };
    useEffect(() => {
        fetchNext();
    }, []);
    return [data, fetchNext];
}
