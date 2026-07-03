import React, { useEffect, useState } from 'react';

export default function PredatorsList() {
    const [predators, setPredators] = useState([[], []]); // [lions, tigers]
    const [error, setError] = useState('');

    useEffect(() => {
        fetch('/api/predators')
            .then(res => res.json())
            .then(data => {
                setPredators(data)
                setError('')
            })
            .catch(err => {
                console.error('Error fetching predators:', err);
                setPredators(null);
                setError(err.message);
            });
    }, []);

    const renderTable = (title, emoji, animals) => (
        <div className="mb-6">
            <h2 className="text-xl font-bold mb-2">
                {emoji} {title}
            </h2>
            <p className="mb-2 text-gray-700">
                Total {title}: <span className="font-semibold">{animals ? animals.length : 0}</span>
            </p>
            {animals && animals.length > 0 ? (
                <table className="table-auto w-full border">
                    <thead>
                    <tr>
                        <th className="border px-2 py-1">Name</th>
                        <th className="border px-2 py-1">Age</th>
                        <th className="border px-2 py-1">Weight (kg)</th>
                        <th className="border px-2 py-1">Gender</th>
                    </tr>
                    </thead>
                    <tbody>
                    {animals.map((a, i) => (
                        <tr key={i}>
                            <td className="border px-2 py-1">{a.name}</td>
                            <td className="border px-2 py-1">{a.age}</td>
                            <td className="border px-2 py-1">{a.weight}</td>
                            <td className="border px-2 py-1">{a.gender}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p className="text-gray-500">No {title.toLowerCase()} yet.</p>
            )}
        </div>
    );

    return (
        <>
            {(error.length > 0) ?
                (<div>{error}</div>) :
                (<div>
                    {renderTable('Lions', '🦁', predators['Lion'])}
                    {renderTable('Tigers', '🐅', predators['Tiger'])}
                </div>)}
        </>
    );
}
