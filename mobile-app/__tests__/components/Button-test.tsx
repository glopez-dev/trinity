import {cleanup, fireEvent, render} from '@testing-library/react-native';
import Button from '@/components/ui/buttons/Button'
import {colors} from "@/lib/constants/Colors";

describe('<Button />', () => {
    afterEach(() => {
        cleanup();
    })
    test('should render correctly the button component with default required props', () => {
        const { getByText } = render(<Button title={'Press Me'}  />);

        getByText('Press Me');
    });

    test('should render correctly the primary button', () => {
        const { rerender, getByRole } = render(
            <Button
                title={'Button Primary'}
                color={'primary'}
            />
        );

        const primaryButton = getByRole('button');
        expect(primaryButton).toBeDefined();
        expect(primaryButton.props.style.backgroundColor).toEqual(colors.primary);

        rerender(
            <Button
                title={'Button Secondary'}
                color={'secondary'}
            />
        )
        const secondaryButton = getByRole('button');
        expect(secondaryButton).toBeDefined();
        expect(secondaryButton.props.style.backgroundColor).toEqual(colors.secondary);

        rerender(
            <Button
                title={'Button Accent'}
                color={'accent'}
            />
        )
        const accentButton = getByRole('button');
        expect(accentButton).toBeDefined();
        expect(accentButton.props.style.backgroundColor).toEqual(colors.accent);
    });

    test('should handle click events', () => {
        const handleClick = jest.fn();
        const { getByRole } = render(<Button title={'Click me'} action={handleClick}/>);

        fireEvent.press(getByRole('button'));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });

    test('should render an icon when specified', () => {
        const { getAllByTestId } = render(<Button title={'Button with Icon'} icon={'Plus'}/>);
        const svgComponent = getAllByTestId('icon-Plus');
        expect(svgComponent).toBeTruthy();
        expect(svgComponent.length).toBeGreaterThan(0);
    })
});
