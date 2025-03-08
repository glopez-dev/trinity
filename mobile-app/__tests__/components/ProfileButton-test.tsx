import ProfileButton from "@/components/feature/profile/button/ProfileButton";
import {cleanup, fireEvent, render} from "@testing-library/react-native";

const mockPush = jest.fn();
jest.mock('expo-router', () => ({
    useRouter: () => ({
        push: mockPush
    })
}));

describe('<ProfileButton />', () => {
    beforeEach(() => {
        cleanup();
        mockPush.mockClear();
    });

    test('should render ProfileButton', () => {
        const { getByText, getByA11yHint } = render(<ProfileButton title={'Informations personnelles'} link={'/profile/informations'} icon={'UserRoundCog'}/>);
        expect(getByText('Informations personnelles')).toBeTruthy();
        expect(getByA11yHint('profile-button')).toBeTruthy();
    });

    test('should navigate to profile screen', () => {
        const { getByA11yHint } = render(<ProfileButton title={'Profile'} link={'/profile/informations'} icon={'UserRoundCog'}/>);
        fireEvent.press(getByA11yHint('profile-button'));
        expect(mockPush).toBeCalledWith('/profile/informations');
    });
});