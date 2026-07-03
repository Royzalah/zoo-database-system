import { useState, useEffect } from 'react';

export default function IncreasingAgeOneYear() {
    const [messages, setMessages] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        fetch('/api/increasingAgeOneYear')
            .then((res) => {
                if (!res.ok) throw new Error("Server returned an error");
                return res.json();
            })
            .then((data) => {
                if (!Array.isArray(data) || data.length === 0) {
                    setError("❌ No valid data received from server.");
                    setMessages([]);
                } else {
                    setMessages(data);
                    setError('');
                }
            })
            .catch((err) => {
                console.error('Error increasingAgeOneYear:', err);
                setError("❌ " + err.message);
                setMessages([]);
            });
    }, []);

    return (
        <div>
            <h2 className="text-3xl font-bold mb-4">Increasing age by One year</h2>

            {error ? (
                <div className="text-red-600 font-semibold">{error}</div>
            ) : (
                <ul className="text-2xl text-black-500 list-disc pl-6">
                    {messages.map((msg, idx) => (
                        <li key={idx}>{msg}</li>
                    ))}
                </ul>
            )}
        </div>
    );
}
