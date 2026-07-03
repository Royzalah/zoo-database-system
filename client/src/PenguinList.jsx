import React, { useEffect, useState } from 'react';

export default function PenguinList() {
    const [penguins, setPenguins] = useState([]);
    const [sortOption, setSortOption] = useState("height");

    const fetchPenguins = (sortBy) => {
        fetch(`/api/penguins?sortBy=${sortBy}`)
            .then(res => res.json())
            .then(data => setPenguins(data))
            .catch(err => console.error('Error fetching penguins:', err));
    };

    useEffect(() => {
        fetchPenguins(sortOption); // ברירת מחדל: לפי גובה
    }, [sortOption]);

    return (
        <div>
            <h2 className="text-xl font-bold mb-4">🐧 Penguin List</h2>

            {/* Selector for sort options */}
            <div className="mb-4">
                <label htmlFor="sort" className="mr-2 font-semibold">Sort by:</label>
                <select
                    id="sort"
                    value={sortOption}
                    onChange={(e) => setSortOption(e.target.value)}
                    className="border px-2 py-1 rounded"
                >
                    <option value="height">Height (desc)</option>
                    <option value="name">Name (asc)</option>
                    <option value="age">Age (asc)</option>
                </select>
            </div>

            {penguins.length > 0 ? (
                <table className="table-auto w-full border">
                    <thead>
                    <tr>
                        <th className="border px-2 py-1">Name</th>
                        <th className="border px-2 py-1">Age</th>
                        <th className="border px-2 py-1">Height (cm)</th>
                    </tr>
                    </thead>
                    <tbody>
                    {penguins.map((p, i) => (
                        <tr key={i}>
                            <td className="border px-2 py-1">
                                {p.name}{" "}
                                {p.leader && (
                                    <span className="text-blue-600 font-semibold">
                                            I can lead the Group
                                        </span>
                                )}
                            </td>
                            <td className="border px-2 py-1">{p.age}</td>
                            <td className="border px-2 py-1">{p.height}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p className="text-gray-500">No penguins found.</p>
            )}
        </div>
    );
}
