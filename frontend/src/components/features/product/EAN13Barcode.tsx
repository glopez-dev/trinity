// EAN13Barcode.tsx
'use client';

import React, {useEffect, useRef} from 'react';

interface EAN13Props {
    value: string;
    width?: number;
    height?: number;
    displayValue?: boolean;
    fontSize?: number;
    fontFamily?: string;
    textMargin?: number;
    background?: string;
    lineColor?: string;
}

const EAN13Barcode: React.FC<EAN13Props> = ({
                                                value,
                                                width = 2,
                                                height = 100,
                                                displayValue = true,
                                                fontSize = 14,
                                                fontFamily = 'monospace',
                                                textMargin = 8,
                                                background = '#FFFFFF',
                                                lineColor = '#000000',
                                            }) => {
    const canvasRef = useRef<HTMLCanvasElement>(null);

    // Vérifier que la valeur est un EAN13 valide
    const isValidEAN13 = (code: string): boolean => {
        // Un EAN13 est toujours composé de 13 chiffres
        if (!/^\d{13}$/.test(code)) {
            return false;
        }

        // Vérification du chiffre de contrôle
        let sum = 0;
        for (let i = 0; i < 12; i++) {
            sum += parseInt(code[i]) * (i % 2 === 0 ? 1 : 3);
        }
        const checkDigit = (10 - (sum % 10)) % 10;

        return checkDigit === parseInt(code[12]);
    };

    // Calculer le chiffre de contrôle pour un EAN-13
    const calculateCheckDigit = (code: string): number => {
        if (!/^\d{12}$/.test(code)) {
            throw new Error('Le code doit être composé de 12 chiffres pour calculer le chiffre de contrôle EAN-13');
        }

        let sum = 0;
        for (let i = 0; i < 12; i++) {
            sum += parseInt(code[i]) * (i % 2 === 0 ? 1 : 3);
        }

        return (10 - (sum % 10)) % 10;
    };

    // Dessiner le code-barres sur le canvas
    const drawEAN13 = (context: CanvasRenderingContext2D, code: string) => {
        // Patterns de codage pour EAN-13
        const leftOddPatterns = [
            '0001101', '0011001', '0010011', '0111101', '0100011',
            '0110001', '0101111', '0111011', '0110111', '0001011'
        ];

        const leftEvenPatterns = [
            '0100111', '0110011', '0011011', '0100001', '0011101',
            '0111001', '0000101', '0010001', '0001001', '0010111'
        ];

        const rightPatterns = [
            '1110010', '1100110', '1101100', '1000010', '1011100',
            '1001110', '1010000', '1000100', '1001000', '1110100'
        ];

        const guardPattern = '101'; // Pattern de garde (début/fin/milieu)
        const middlePattern = '01010'; // Pattern central

        // Structure de codage L/G selon le premier chiffre
        const firstDigitEncoding = [
            'LLLLLL', 'LLGLGG', 'LLGGLG', 'LLGGGL', 'LGLLGG',
            'LGGLLG', 'LGGGLL', 'LGLGLG', 'LGLGGL', 'LGGLGL'
        ];

        // Largeur totale calculée
        const unitWidth = width;
        const fullWidth = unitWidth * 95 + (displayValue ? 0 : 0); // 95 unités pour un EAN-13 complet
        const fullHeight = height;

        const firstDigit = parseInt(code[0]);
        const encoding = firstDigitEncoding[firstDigit];

        // Calcul des dimensions du canvas
        const canvasWidth = fullWidth;
        const canvasHeight = fullHeight + (displayValue ? fontSize + textMargin : 0);

        if (canvasRef.current) {
            canvasRef.current.width = canvasWidth;
            canvasRef.current.height = canvasHeight;
        }

        // Dessiner le fond
        context.fillStyle = background;
        context.fillRect(0, 0, canvasWidth, canvasHeight);

        context.fillStyle = lineColor;

        let xPos = 0;

        // Dessiner le pattern de garde de début
        drawPattern(context, guardPattern, xPos, 0, unitWidth, fullHeight);
        xPos += guardPattern.length * unitWidth;

        // Dessiner la première moitié (6 chiffres après le premier chiffre)
        for (let i = 1; i <= 6; i++) {
            const digit = parseInt(code[i]);
            const pattern = encoding[i - 1] === 'L' ? leftOddPatterns[digit] : leftEvenPatterns[digit];
            drawPattern(context, pattern, xPos, 0, unitWidth, fullHeight);
            xPos += pattern.length * unitWidth;
        }

        // Dessiner le pattern central
        drawPattern(context, middlePattern, xPos, 0, unitWidth, fullHeight);
        xPos += middlePattern.length * unitWidth;

        // Dessiner la seconde moitié (6 derniers chiffres)
        for (let i = 7; i <= 12; i++) {
            const digit = parseInt(code[i]);
            drawPattern(context, rightPatterns[digit], xPos, 0, unitWidth, fullHeight);
            xPos += rightPatterns[digit].length * unitWidth;
        }

        // Dessiner le pattern de garde de fin
        drawPattern(context, guardPattern, xPos, 0, unitWidth, fullHeight);

        // Afficher les chiffres sous le code-barres
        if (displayValue) {
            context.font = `${fontSize}px ${fontFamily}`;
            context.textAlign = 'center';
            context.fillStyle = lineColor;

            // Premier chiffre (à gauche)
            context.fillText(code[0], unitWidth * 7, fullHeight + fontSize);

            // Premier groupe de 6 chiffres
            for (let i = 1; i <= 6; i++) {
                const digitX = unitWidth * (3 + 7 * i);
                context.fillText(code[i], digitX, fullHeight + fontSize);
            }

            // Second groupe de 6 chiffres
            for (let i = 7; i <= 12; i++) {
                const digitX = unitWidth * (3 + 7 * i + 5); // +5 pour le pattern central
                context.fillText(code[i], digitX, fullHeight + fontSize);
            }
        }
    };

    // Fonction utilitaire pour dessiner un pattern
    const drawPattern = (
        context: CanvasRenderingContext2D,
        pattern: string,
        x: number,
        y: number,
        width: number,
        height: number
    ) => {
        for (let i = 0; i < pattern.length; i++) {
            if (pattern[i] === '1') {
                context.fillRect(x + i * width, y, width, height);
            }
        }
    };

    // Générer et valider le code-barres
    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const context = canvas.getContext('2d');
        if (!context) return;

        let codeToUse = value.replace(/\D/g, ''); // Conserver uniquement les chiffres

        // Compléter à 12 chiffres si nécessaire
        if (codeToUse.length < 12) {
            codeToUse = codeToUse.padStart(12, '0');
        }
        // Tronquer si plus de 12 chiffres (hors check digit)
        else if (codeToUse.length > 12) {
            codeToUse = codeToUse.substring(0, 12);
        }

        // Calculer et ajouter le chiffre de contrôle
        const checkDigit = calculateCheckDigit(codeToUse);
        const fullCode = codeToUse + checkDigit;

        // Dessiner le code-barres
        drawEAN13(context, fullCode);
    }, [value, width, height, displayValue, fontSize, fontFamily, textMargin, background, lineColor]);

    return (
        <div style={{display: 'inline-block'}}>
            <canvas ref={canvasRef}/>
        </div>
    );
};

export default EAN13Barcode;