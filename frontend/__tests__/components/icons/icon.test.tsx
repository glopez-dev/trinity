import {afterEach, describe, expect, it, vi} from "vitest";
import {cleanup, fireEvent, render} from "@testing-library/react";
import Icon from "@/components/ui/icons/Icon";
import {icons} from "lucide-react";

interface IconArrayItem {
    name: keyof typeof icons;
    class: string;
}

type IconArray = IconArrayItem[];

describe('Icon Component', () => {

    afterEach(() => {
        cleanup();
    })
    it('renders correctly with default props', () => {
        render(<Icon name={'Plus'}/>)
        const icon = document.querySelector('svg');
        expect(icon).toBeDefined();
        expect(icon?.getAttribute('class')).toContain('lucide lucide-plus');
    });

    it('should apply custom styles correctly', () => {
        const {container} = render(
            <Icon
                name="Heart"
                size={24}
                className="custom-class"
            />
        );

        const svg = container.querySelector('svg');
        expect(svg?.getAttribute('width')).toBe('24');
        expect(svg?.getAttribute('height')).toBe('24');
        expect(svg?.classList.contains('custom-class')).toBe(true);
    });

    // Test avec différentes icônes
    it('should render different icons correctly', () => {
        const iconNames: IconArray = [
            {
                name: 'Heart',
                class: 'lucide-heart'
            },
            {
                name: 'Circle',
                class: 'lucide-circle'
            }
        ];

        iconNames.forEach((item) => {
            const {container} = render(<Icon name={item.name}/>);
            const svg = container.querySelector('svg');
            expect(svg?.classList.contains(item.class)).toBeTruthy();
        });
    });

    // Test des événements
    it('should handle click events', () => {
        const handleClick = vi.fn();
        const {container} = render(
            <Icon name="Bell" onClick={handleClick}/>
        );
        fireEvent.click(container.querySelector('svg')!);
        expect(handleClick).toHaveBeenCalledTimes(1);
    });

    // Test des valeurs par défaut
    it('should have default size if not specified', () => {
        const {container} = render(<Icon name="User"/>);
        const svg = container.querySelector('svg');

        expect(svg?.getAttribute('width')).toBe('24');
        expect(svg?.getAttribute('height')).toBe('24');
    });

    // Test avec stroke-width personnalisé
    it('should apply custom strokeWidth', () => {
        const {container} = render(
            <Icon name="Circle" strokeWidth={3}/>
        );

        const svg = container.querySelector('svg');
        expect(svg?.getAttribute('stroke-width')).toBe('3');
    });

});