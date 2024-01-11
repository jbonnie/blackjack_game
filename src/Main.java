import java.util.*;

public class Main {
    public static int[] card = new int[14];         // 숫자카드 당 몇 번 사용되었는지 카운트
    public static boolean[] done;       // 플레이어 별로 카드 받기가 끝났는지 저장
    public static int[] sum;        // 플레이어 별 카드합 저장

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("플레이어의 수를 입력하세요: ");
        int N = Integer.parseInt(sc.nextLine());
        done = new boolean[N+1];
        sum = new int[N+1];

        int turn = 1;
        while(!endOrNot()) {
            System.out.println("Turn #" + turn + ": ");
            turn++;
            // auto player 먼저 실행
            for(int i = 1; i < N; i++) {
                boolean get = autoPlayerDecision(i);
                if(get) {
                    int num = shuffle();
                    sum[i] += num;
                    if(sum[i] > 21) {       // 합이 21을 초과하여 탈락
                        done[i] = true;
                    }
                }
                else {
                    done[i] = true;     // 카드 받기 종료
                }
            }
            // 유저 차례
            if(done[N]) continue;
            System.out.println("카드를 받으시겠습니까? (yes / no): ");
            String answer = sc.nextLine();
            if(answer.equals("yes")) {
                int num = shuffle();
                sum[N] += num;
                if(sum[N] > 21) {
                    done[N] = true;
                    System.out.println("21을 초과하였습니다. 게임 종료");
                }
            }
            else {
                done[N] = true;
            }

            // 턴 종료 후 전체 플레이어의 카드합 현황 출력
            System.out.println("턴이 종료되었습니다. 플레이어들의 카드합 현황: ");

            for(int i = 1; i <= N; i++) {
                System.out.println("플레이어 " + i + ": "  + sum[i] + ", 카드 받기 진행 중: " + !done[i]);
            }
        }
        // 게임 종료
        System.out.println("게임이 종료되었습니다. 우승자는 : ");
        ArrayList<Integer> winner = new ArrayList<Integer>();
        int max = -1;
        for(int i = 1; i < sum.length; i++) {
            if(sum[i] > 21) {
                continue;
            }
            if(max < sum[i]) {
                max = sum[i];
                winner = new ArrayList<Integer>();
                winner.add(i);
            }
            else if(max == sum[i]) {
                winner.add(i);
            }
        }
        if(max == -1) {
            System.out.println("없습니다.");
        }
        else {
            for(int i = 0; i < winner.size(); i++) {
                if(i != winner.size()-1)
                    System.out.print("플레이어 " + winner.get(i) + ", ");
                else
                    System.out.println("플레이어 " + winner.get(i) + "입니다.");
            }
        }
    }

    // 딜러가 랜덤하게 숫자카드를 선택하여 리턴
    public static int shuffle() {
        while(true) {
            int num = (int)(Math.random() * 13 + 1);
            if(card[num] < 4) {
                card[num]++;
                return num;
            }
        }
    }
    // 컴퓨터 플레이어가 확률을 계산하여 카드를 받을지 받지 말지 결정하는 함수
    public static boolean autoPlayerDecision(int index) {
        int remain = 21 - sum[index];
        int total_num = 0;
        int good_num = 0;
        for(int i = 1; i <= 13; i++) {
            total_num += 4-card[i];
            if(i <= remain) {
                good_num += 4-card[i];
            }
        }
        double rate = (double)good_num / total_num;
        if(rate >= 0.5)
            return true;
        else
            return false;
    }

    // 게임이 끝났는지 확인하는 함수
    public static boolean endOrNot() {
        for(int i = 1; i < done.length; i++) {
            if(!done[i])
                return false;
        }
        return true;
    }
}