import React, { useEffect, useState } from 'react';

export default function VeterinaryClinicList() {
    const [clinicData, setClinicData] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        fetch('/api/veterinaryClinic')
            .then(res => res.json())
            .then(data => {
                const transformed = Object.entries(data).map(([animal, treatments]) => ({
                    animal,
                    treatments
                }));
                setClinicData(transformed);
                setError('');
            })
            .catch(err => {
                console.error('Error fetching veterinary clinic data:', err);
                setClinicData([]);
                setError(err.message);
            });
    }, []);

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">🏥 Veterinary Clinic</h2>
            {error.length > 0 ? (
                <div className="text-red-500">{error}</div>
            ) : clinicData && clinicData.length > 0 ? (
                <div className="grid gap-4 md:grid-cols-2">
                    {clinicData.map((entry, idx) => (
                        <div key={idx} className="p-4 bg-gray-100 rounded shadow">
                            <h3 className="text-lg font-bold mb-2">{entry.animal}</h3>
                            <h4 className="font-semibold">Treatments:</h4>
                            {entry.treatments && entry.treatments.length > 0 ? (
                                <ul className="list-disc ml-5 text-sm text-gray-700">
                                    {entry.treatments.map((t, tIdx) => (
                                        <li key={tIdx}>
                                            {t.date} – {t.description}
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p className="text-gray-500 text-sm">No treatments yet.</p>
                            )}
                        </div>
                    ))}
                </div>
            ) : (
                <p className="text-gray-500">No animals under treatment.</p>
            )}
        </div>
    );
}
