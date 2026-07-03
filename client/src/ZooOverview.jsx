import React, {useEffect, useState} from 'react';

export default function ZooOverview() {
    const [zooData, setZooData] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        fetch('/api/zoo')
            .then(res => res.json())
            .then(data => {
                setZooData(data)
                setError('')
            })
            .catch(err => {
                console.error('Error fetching zoo data:', err);
                setZooData(null);
                setError(err.message);
            });
    }, []);

    function ZooBanner() {
        return (
            <div className="w-full h-[150px] overflow-hidden rounded-xl shadow">
                <img
                    src="/images/zoo.png"
                    alt="Zoo banner"
                    className="w-full h-full object-cover"
                    style={{objectPosition: 'center 45%'}}
                />
            </div>
        );
    }

    return (
        <>
            {(error.length > 0) ?
                (<div>{error}</div>) :
                (<div className="space-y-4">
                <div className="relative w-full h-[150px] overflow-hidden rounded-xl shadow">
                    <img src="/images/zoo.png" alt="Zoo banner" className="absolute inset-0 w-full h-full object-cover"
                         style={{objectPosition: 'center 45%'}}/>
                    <div className="absolute inset-0 bg-gradient-to-r from-black/20 to-transparent"/>
                    <h4 className="absolute left-1 bottom-1 text-white text-2xl font-bold drop-shadow">Java OOP - Summer
                        2025</h4>
                </div>
                <div>
                    <h2 className="text-2xl font-bold mb-4">Zoo Overview</h2>
                    {zooData ? (
                        <>
                            <h2 className="text-lg font-bold mb-4">
                                {zooData.details}
                            </h2>

                            <div className="flex flex-col gap-4">
                                {/* Predators Section */}
                                <div className="p-3 bg-gray-100 rounded shadow">
                                    <h3 className="font-semibold">Predators</h3>
                                    <ul className="list-disc ml-5">
                                        <li>🦁 Lions: {zooData.predators?.[0] ?? 0}</li>
                                        <li>🐅 Tigers: {zooData.predators?.[1] ?? 0}</li>
                                    </ul>
                                </div>

                                {/* Penguins Section */}
                                <div className="p-3 bg-gray-100 rounded shadow">
                                    <h3 className="font-semibold">🐧 Penguins</h3>
                                    <p>Total: {zooData.penguins}</p>
                                </div>

                                {/* Aquarium Section */}
                                <div className="p-3 bg-gray-100 rounded shadow">
                                    <h3 className="font-semibold">Fish</h3>
                                    <ul className="list-disc ml-5">
                                        <li>🐠 Aquarium Fish: {zooData.fish?.[0] ?? 0}</li>
                                        <li>✨ GoldFish: {zooData.fish?.[1] ?? 0}</li>
                                        <li>🎭 ClownFish: {zooData.fish?.[2] ?? 0}</li>
                                    </ul>
                                </div>
                                {/* Veterinary Clinic Section */}
                                <div className="p-3 bg-gray-100 rounded shadow">
                                    <h3 className="font-semibold">🏥 Veterinary Clinic</h3>
                                    <p>Total sick animals: {zooData.veterinarySummary?.totalSick ?? 0}</p>

                                    {zooData.veterinarySummary?.byType &&
                                        Object.entries(zooData.veterinarySummary.byType).length > 0 && (
                                            <ul className="list-disc ml-5">
                                                {Object.entries(zooData.veterinarySummary.byType).map(([type, count], idx) => (
                                                    <li key={idx}>
                                                        {type}: {count}
                                                    </li>
                                                ))}
                                            </ul>
                                        )}
                                </div>

                            </div>
                        </>
                    ) : (
                        <p className="text-gray-500">Loading zoo data...</p>
                    )}
                </div>
            </div>)}
        </>
    );
}
