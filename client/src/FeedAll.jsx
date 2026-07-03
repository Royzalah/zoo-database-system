import { useState, useEffect } from 'react';

export default function FeedAll() {
    const [feedAllRes, setFeedAllRes] = useState([]);

    useEffect(() => {
        fetch('/api/feedAll')
            .then((res) => res.json())   // עכשיו JSON ולא text
            .then((data) => {
                setFeedAllRes(data);
            })
            .catch((err) => console.error('Error fetching feedAllRes:', err));
    }, []);

    return (
        <div>
            <h2 className="text-3xl font-bold mb-4">🍽️ Feeding all animals</h2>
            <ul className="text-2xl text-black-500 list-disc pl-6">
                {feedAllRes.map((item, index) => (
                    <li key={index}>
                        {item.animalType} eat {item.amount} {item.unit}
                    </li>
                ))}
            </ul>
        </div>
    );
}
