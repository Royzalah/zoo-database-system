import React, {useEffect, useState} from 'react';

export default function LoadZooData() {
    const [response, setResponse] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        fetch('/api/loadData')
            .then((res) => res.text())
            .then((data) => {
                setResponse(data);
            })
            .catch((err) =>{
                console.error("Error loading zoo data:", err)
                setError(err.message);
            });
    }, []);

    return (
        <>
            {(error.length > 0) ?
                (<div>{error}</div>) :
                (<div className="space-y-4">{response}</div>)}
        </>
    );
}
