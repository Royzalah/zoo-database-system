import { useState, useEffect } from 'react';

export default function ExitZoo() {
    const [message, setMessage] = useState('Saving data...');

    useEffect(() => {
        fetch('/api/exit')
            .then((res) => res.text())
            .then((data) => {
                setMessage(<> {data} <br/> 👋 Goodbye! You may now close the window. </>);
            })
            .catch((err) => {
                console.error('Error saving zoo data:', err);
                setMessage("❌ Failed to save zoo data.");
            });
    }, []);

    return (
        <div className="text-xl font-semibold text-center mt-8">
            {message}
        </div>
    );
}
