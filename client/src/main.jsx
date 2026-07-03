import React from 'react';
import ReactDOM from 'react-dom/client';
import ZooApp from './ZooApp';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <div className="mx-auto w-full md:w-[90%]">
            <ZooApp />
        </div>
    </React.StrictMode>
);
