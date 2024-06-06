package kr.or.ddit.basic;

public class T02StaticBlockTest {
	
	/*
	 Static 블록 : 클래스가 처음 로드될 때 한 번 실행
	 인스턴스 블록 : 객체가 생성될 때마다 생성자 호출 전에 실행
	 생성자 : 인스턴스 블록이 실행된 후에 호출
	 
	 따라서, 인스턴스 블록이 생성자보다 먼저 호출되는 이유는 
	 객체 초기화 과정에서 인스턴스 블록은 생성자 호출 전에 실행되도록 설계되어 있기 때문입니다. 
	 인스턴스 블록은 주로 객체 생성 시 공통적으로 수행해야 하는 초기화 작업을 처리하는 데 사용됩니다.
	 */
	
	public T02StaticBlockTest() {
		System.out.println("생성자 호출됨");
	}
	
	static {
		System.out.println("첫번째 static block 호출됨");
	}
	
	static {
		System.out.println("두번째 static block 호출됨");
	}
	
	{
		System.out.println("첫번째 intance block 호출됨");
	}

	{
		System.out.println("두번째 intance block 호출됨");
	}
	
	public static void main(String[] args) {
		
		new T02StaticBlockTest();
//		
//		new T02StaticBlockTest();
	}
	
}
